/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.rewan.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rewan.data.MovieContract.MovieEntry;


public class MovieProvider extends ContentProvider {

        public static final int CODE_MOVIE = 100;
        public static final int CODE_MOVIE_WITH_ID = 101;

        private static final UriMatcher sUriMatcher = buildUriMatcher();
        private MovieDbHelper mOpenHelper;


        private static UriMatcher buildUriMatcher() {

            final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
            final String authority = MovieContract.CONTENT_AUTHORITY;

            matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
            matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

            return matcher;
        }

        @Override
        public boolean onCreate() {
            mOpenHelper = new MovieDbHelper(getContext());
            return true;
        }

        @Override
        public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

            switch (sUriMatcher.match(uri)) {
                case CODE_MOVIE:
                    db.beginTransaction();
                    int rowsInserted = 0;
                    try {
                        for(ContentValues value: values) {
                            long _id = db.insert(MovieEntry.TABLE_NAME, null, value);
                            if(_id!=-1) {
                                rowsInserted++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    if(rowsInserted > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return rowsInserted;
                default:
                    return super.bulkInsert(uri, values);
            }
        }

        @Nullable
        @Override
        public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

            final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

            Cursor cursor;

            switch (sUriMatcher.match(uri)) {
                case CODE_MOVIE: {

                    cursor = db.query(MovieEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                }

                case CODE_MOVIE_WITH_ID: {
                    String id = uri.getLastPathSegment();
                    String[] selectionArguments = new String[] {id};

                    cursor = db.query(MovieEntry.TABLE_NAME,
                            projection,
                            MovieEntry.COLUMN_MOVIE_ID+ " = ?",
                            selectionArguments,
                            null,
                            null,
                            sortOrder);

                    break;
                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);

            }

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        @Override
        public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

            int numRowsDeleted;

            if( null == selection) selection = "1";

            switch (sUriMatcher.match(uri)) {
                case CODE_MOVIE: {
                    numRowsDeleted = db.delete(MovieEntry.TABLE_NAME,
                            selection,
                            selectionArgs);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);

            }

            if(numRowsDeleted != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return numRowsDeleted;
        }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        int recordId;
        switch(match) {
            case CODE_MOVIE:
                db = mOpenHelper.getWritableDatabase();
                recordId = (int) (db.insert(MovieEntry.TABLE_NAME, null, values));
                if(recordId >=0) {
                    returnUri = MovieEntry.buildMovieUriWithId(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        if (recordId >= 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;

    }

        @Override
        public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
            throw new RuntimeException();
        }

        @Nullable
        @Override
        public String getType(@NonNull Uri uri) {
            throw new RuntimeException();
        }
    }
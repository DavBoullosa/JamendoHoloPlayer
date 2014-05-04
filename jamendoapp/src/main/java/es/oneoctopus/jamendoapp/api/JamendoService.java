/*
 * Copyright (c) 2014 David Alejandro Fernández Sancho
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.oneoctopus.jamendoapp.api;

import es.oneoctopus.jamendoapp.interfaces.RestMethods;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public class JamendoService {
    private static final String API_URL = "http://api.jamendo.com/v3.0";

    public static RestMethods getService() {
        return new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setConverter(new JacksonConverter())
                .build()
                .create(RestMethods.class);
    }
}

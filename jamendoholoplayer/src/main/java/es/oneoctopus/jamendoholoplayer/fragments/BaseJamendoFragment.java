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

package es.oneoctopus.jamendoholoplayer.fragments;

import android.support.v4.app.Fragment;

import es.oneoctopus.jamendoholoplayer.api.JamendoService;
import es.oneoctopus.jamendoholoplayer.api.RestMethods;

public class BaseJamendoFragment extends Fragment {
    private RestMethods api;

    public RestMethods getApi() {
        if (api == null) {
            api = JamendoService.getService();
        }
        return api;
    }

    public void setApi(RestMethods jamendoApi) {
        api = jamendoApi;
    }

}

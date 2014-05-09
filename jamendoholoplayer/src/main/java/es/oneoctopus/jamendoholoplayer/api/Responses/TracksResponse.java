
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

package es.oneoctopus.jamendoholoplayer.api.Responses;

import java.util.List;

public class TracksResponse {
    private Headers headers;
    private List results;

    public Headers getHeaders() {
        return this.headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public List getResults() {
        return this.results;
    }

    public void setResults(List results) {
        this.results = results;
    }
}

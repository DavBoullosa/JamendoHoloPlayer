
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

package es.oneoctopus.jamendoapp.api.Responses;

public class Headers {
    private Number code;
    private String error_message;
    private Number results_count;
    private String results_fullcount;
    private String status;
    private String warnings;

    public Number getCode() {
        return this.code;
    }

    public void setCode(Number code) {
        this.code = code;
    }

    public String getError_message() {
        return this.error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public Number getResults_count() {
        return this.results_count;
    }

    public void setResults_count(Number results_count) {
        this.results_count = results_count;
    }

    public String getResults_fullcount() {
        return this.results_fullcount;
    }

    public void setResults_fullcount(String results_fullcount) {
        this.results_fullcount = results_fullcount;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarnings() {
        return this.warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

}

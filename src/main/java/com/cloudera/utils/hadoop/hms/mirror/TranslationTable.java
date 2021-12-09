/*
 * Copyright 2021 Cloudera, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.hadoop.hms.mirror;

public class TranslationTable {

    /**
     * Optional, when defined, rename the table.
     *
     * The rename WILL affect the location IF:
     * - the location is NOT specified AND your NOT running in distcpCompatible mode.
     *
     * When the location is not specified, a rename will use the new standard location as
     * a basis for the location.
     */
    private String rename;
    /*
    Optional, when specified, this will override all other hierarchy
    attempts to set this.
     */
    private String location;

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

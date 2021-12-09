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

import java.util.*;

public class EnvironmentTable {

    private String name = null;
    private Boolean exists = Boolean.FALSE;
    private CreateStrategy createStrategy = CreateStrategy.NOTHING;
    private List<String> definition = new ArrayList<String>();
    private Boolean partitioned = Boolean.FALSE;
    private List<String> partitions = new ArrayList<String>();
    private List<String> actions = new ArrayList<String>();
    private Map<String, String> addProperties = new TreeMap<String, String>();
    private List<String> issues = new ArrayList<String>();
    private List<Pair> sql = new ArrayList<Pair>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public CreateStrategy getCreateStrategy() {
        return createStrategy;
    }

    public void setCreateStrategy(CreateStrategy createStrategy) {
        this.createStrategy = createStrategy;
    }

    public List<String> getDefinition() {
        return definition;
    }

    public void setDefinition(List<String> definition) {
        this.definition = definition;
    }

    public Boolean getPartitioned() {
        return partitions.size() > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    public List<String> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<String> partitions) {
        this.partitions = partitions;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public void addAction(String action) {
        getActions().add(action);
    }

    public Map<String, String> getAddProperties() {
        return addProperties;
    }

    public void addProperty(String key, String value) {
        getAddProperties().put(key, value);
    }
    public void setAddProperties(Map<String, String> addProperties) {
        this.addProperties = addProperties;
    }

    public List<String> getIssues() {
        return issues;
    }

    public void addIssue(String issue) {
        getIssues().add(issue);
    }

    public void setIssues(List<String> issues) {
        this.issues = issues;
    }

    public List<Pair> getSql() {
        return sql;
    }

    public void addSql(Pair sqlPair) {
        getSql().add(sqlPair);
    }

    public void addSql(String desc, String sql) {
        Pair pair = new Pair(desc, sql);
        addSql(pair);
    }
}

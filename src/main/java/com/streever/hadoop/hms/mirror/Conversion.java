package com.streever.hadoop.hms.mirror;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Conversion {
    private Date start = new Date();
    private Map<String, DBMirror> databases = new TreeMap<String, DBMirror>();

    public DBMirror addDatabase(String database) {
        if (databases.containsKey(database)) {
            return databases.get(database);
        } else {
            DBMirror dbs = new DBMirror(database);
            databases.put(database, dbs);
            return dbs;
        }
    }

    public Map<String, DBMirror> getDatabases() {
        return databases;
    }

    public DBMirror getDatabase(String database) {
        return databases.get(database);
    }

    public String toReport(Config config) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        Set<String> databaseSet = databases.keySet();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("# HMS-Mirror  ");
        sb.append(ReportingConf.substituteVariablesFromManifest("v.${Implementation-Version}")).append("\n");
        sb.append("---\n").append("## Run Log\n\n");
        sb.append("| Date | Elapsed Time |\n");
        sb.append("|:---|:---|\n");
        Date current = new Date();
        BigDecimal elsecs = new BigDecimal(current.getTime()-start.getTime()).divide(new BigDecimal(1000));
        DecimalFormat eldecf = new DecimalFormat("#,###.00");
        String elsecStr = eldecf.format(elsecs);

        sb.append("| ").append(df.format(new Date())).append(" | ").append(elsecStr).append(" secs |\n\n");

        sb.append("## Config:\n");

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String yamlStr = mapper.writeValueAsString(config);
        sb.append("```\n");
        sb.append(yamlStr).append("\n");
        sb.append("```\n");

        sb.append("## Databases\n");
        for (String database : databaseSet) {
            sb.append("## ").append(database).append("\n");
            DBMirror dbMirror = databases.get(database);
            sb.append("\n");

            sb.append("|").append(" Table ").append("|")
                    .append("Phase<br/>State").append("|")
                    .append("Phase<br/>Duration").append("|")
                    .append("Partition<br/>Count").append("|")
                    .append("Actions").append("|")
                    .append("Added<br/>Properties").append("|")
                    .append("Issues").append("|")
                    .append("\n");
            sb.append("|").append(":---").append("|")
                    .append(":---").append("|")
                    .append("---:").append("|")
                    .append("---:").append("|")
                    .append(":---").append("|")
                    .append(":---").append("|")
                    .append(":---").append("|")
                    .append("\n");
            Set<String> tables = dbMirror.getTableMirrors().keySet();
            for (String table : tables) {
                TableMirror tblMirror = dbMirror.getTableMirrors().get(table);
                sb.append("|").append(table).append("|")
                        .append(tblMirror.getPhaseState().toString()).append("|");

                // Stage Duration
                BigDecimal secs = new BigDecimal(tblMirror.getStageDuration()).divide(new BigDecimal(1000));///1000
                DecimalFormat decf = new DecimalFormat("#,###.00");
                String secStr = decf.format(secs);
                sb.append(secStr).append(" secs |");

                // Partition Count
                sb.append(tblMirror.getPartitionDefinition(Environment.LOWER) != null ?
                        tblMirror.getPartitionDefinition(Environment.LOWER).size() : " ").append("|");

                // Actions
                Iterator<Map.Entry<String, Object>> aIter = tblMirror.getActions().entrySet().iterator();
                while (aIter.hasNext()) {
                    Map.Entry<String, Object> item = aIter.next();
                    sb.append(item.getKey()).append(" -> ");
                    if (item.getValue() != null)
                        sb.append(item.getValue().toString());
                    if (aIter.hasNext())
                        sb.append("<br/>");
                }
                sb.append("|");
                // Properties
                if (tblMirror.whereTherePropsAdded()) {
                    for (String propAdd : tblMirror.getPropAdd()) {
                        sb.append(propAdd).append("<br/>");
                    }
                } else {
                    sb.append(" ");
                }
                sb.append("|");
                // Issues
                if (tblMirror.isThereAnIssue()) {
                    for (String issue : tblMirror.getIssues()) {
                        sb.append(issue).append("<br/>");
                    }
                } else {
                    sb.append(" ");
                }
                sb.append("|\n");
            }

        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Conversion:").append("\n");
        sb.append("\tDatabases : ").append(databases.size()).append("\n");
        int tables = 0;
        int partitions = 0;
        for (String database : databases.keySet()) {
            DBMirror db = databases.get(database);
            tables += db.getTableMirrors().size();
            for (String table : db.getTableMirrors().keySet()) {
                if (db.getTableMirrors().get(table).getPartitionDefinition(Environment.LOWER) != null) {
                    partitions += db.getTableMirrors().get(table).getPartitionDefinition(Environment.LOWER).size();
                }
            }
        }
        sb.append("\tTables    : " + tables).append("\n");
        sb.append("\tPartitions: " + partitions);

        return sb.toString();
    }
}

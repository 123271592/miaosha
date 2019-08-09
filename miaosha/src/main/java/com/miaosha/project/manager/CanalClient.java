package com.miaosha.project.manager;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanalClient {

    public static Map<String,Long> topicOffSet = new HashMap<String,Long>();

    public static void main(String args[]){
        CanalConnector connector = CanalConnectors.newClusterConnector("127.0.0.1:2181",
                                                                        "example",
                                                                        "",
                                                                        "");

        try{
            connector.connect();

            connector.subscribe(".*\\..*");

            while(true){
                Message message = connector.getWithoutAck(100);

                long batchId = message.getId();
                int batchSize = message.getEntries().size();

                if(batchId == -1 || batchSize == 0){
                    Thread.sleep(1000);
                }else{
                    printEntry(message.getEntries());
                }

                connector.ack(batchId);
                //connector.rollback(batchId);
            }
        }catch (Exception e){
            System.out.print(e.getStackTrace());
        }finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<CanalEntry.Entry> entries) {

        for(CanalEntry.Entry entry : entries){
            if(entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND ){
                continue;
            }

            topicOffSet.put("example",entry.getHeader().getLogfileOffset());

            CanalEntry.RowChange rowChange = null;
            try{
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            }catch(Exception e){

            }

            CanalEntry.EventType eventType = rowChange.getEventType();
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }
    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

}


package com.alejandro.servidormonitoreosalud.controller;

import com.alejandro.servidormonitoreosalud.model.ConnectionDB;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;


public class DBController {
    
    
    
    public static void WriteData(Point point){
        WriteApi writeApi = ConnectionDB.instance.CLIENT.makeWriteApi(WriteOptions.DEFAULTS);
        writeApi.writePoint(point);
        writeApi.flush();
    }
    
    
    public static void SendSensorInfo(SensorInfo info){
        Point point = Point.measurement(info.getId())
                .addField("temperatura", info.getTemperatura())
                .addField("ritmoCardiaco", info.getRitmoCardiaco())
                .addField("presionArterial", info.getPresionArteril())
                .time(Instant.now(), WritePrecision.MS); // Timestamp actual

        // Mandar datos a escribir
        WriteData(point);
    }
    
    
    public static FluxRecord[] GetData(String document){
        
        QueryApi queryApi = ConnectionDB.instance.CLIENT.getQueryApi();
        
        String flux = String.format(
                "from(bucket:\"%s\") |> range(start: -12h) |> filter(fn: (r) => r._measurement == \""+document+"\")", 
                ConnectionDB.getINF_BUCKET());
        
        List<FluxTable> tables = queryApi.query(flux, ConnectionDB.getINF_ORG());
        
        FluxRecord[] resultRecords = new FluxRecord[0];
        
        for (FluxTable tbl: tables ) {
            for (FluxRecord record : tbl.getRecords()) {
                int index = resultRecords.length;
                resultRecords =  Arrays.copyOf(resultRecords, index+1);
                resultRecords[index] = record;
            }
        }
        
        return resultRecords;
    }
    
    public static SensorInfo GetSensorInfoData(String id){
        
        FluxRecord[] records = GetData(id);
        SensorInfo info = new SensorInfo(id,0,0,0);
        for (int i = 0; i < records.length; i++) {
            if (records[i].getField().equals("presionArterial")) {
                info.setPresionArteril((double) records[i].getValue());
            }
            if (records[i].getField().equals("ritmoCardiaco")) {
                info.setRitmoCardiaco((double) records[i].getValue());
            }
            if (records[i].getField().equals("temperatura")) {
                info.setTemperatura((double) records[i].getValue());
            }
        }
        
        return info;
    }
     
    
}

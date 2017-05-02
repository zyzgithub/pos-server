package com.dianba.supplychain.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "supply_chain_warehouse_relationship")
public class BarcodeRelationship implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer sourceId;

    private String sourceBarcode;

    private Integer sourceUnitId;

    private Integer sourceConversionCount;

    private String targetBarcode;

    private Integer targetUnitId;

    private Integer targetConversionCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceBarcode() {
        return sourceBarcode;
    }

    public void setSourceBarcode(String sourceBarcode) {
        this.sourceBarcode = sourceBarcode == null ? null : sourceBarcode.trim();
    }

    public Integer getSourceUnitId() {
        return sourceUnitId;
    }

    public void setSourceUnitId(Integer sourceUnitId) {
        this.sourceUnitId = sourceUnitId;
    }

    public Integer getSourceConversionCount() {
        return sourceConversionCount;
    }

    public void setSourceConversionCount(Integer sourceConversionCount) {
        this.sourceConversionCount = sourceConversionCount;
    }

    public String getTargetBarcode() {
        return targetBarcode;
    }

    public void setTargetBarcode(String targetBarcode) {
        this.targetBarcode = targetBarcode == null ? null : targetBarcode.trim();
    }

    public Integer getTargetUnitId() {
        return targetUnitId;
    }

    public void setTargetUnitId(Integer targetUnitId) {
        this.targetUnitId = targetUnitId;
    }

    public Integer getTargetConversionCount() {
        return targetConversionCount;
    }

    public void setTargetConversionCount(Integer targetConversionCount) {
        this.targetConversionCount = targetConversionCount;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}

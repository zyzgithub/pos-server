package com.dianba.supplychain.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "supply_chain_barcode_relationship")
public class BarcodeRelationship implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "source_id")
    private Integer sourceId;

    @Column(name = "source_barcode")
    private String sourceBarcode;

    @Column(name = "source_unit_id")
    private Integer sourceUnitId;

    @Column(name = "source_conversion_count")
    private Integer sourceConversionCount;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "target_barcode")
    private String targetBarcode;

    @Column(name = "target_unit_id")
    private Integer targetUnitId;

    @Column(name = "target_conversion_count")
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}

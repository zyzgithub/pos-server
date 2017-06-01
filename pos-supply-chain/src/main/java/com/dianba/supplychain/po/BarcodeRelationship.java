package com.dianba.supplychain.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "life_item.item_relationship")
public class BarcodeRelationship implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "source_barcode")
    private String sourceBarcode;

    @Column(name = "target_barcode")
    private String targetBarcode;

    @Column(name = "target_unit_id")
    private Integer targetUnitId;

    @Column(name = "relation_coefficient")
    private Integer relationCoefficient;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRelationCoefficient() {
        return relationCoefficient;
    }

    public void setRelationCoefficient(Integer relationCoefficient) {
        this.relationCoefficient = relationCoefficient;
    }
}

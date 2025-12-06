package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "tag")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagModel extends BaseModel {

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<ProductModel> products = new ArrayList<>();
}

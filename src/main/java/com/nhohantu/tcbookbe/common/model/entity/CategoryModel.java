package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "category")
@Entity
public class CategoryModel extends BaseModel {

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;//tên danh mục

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryModel parentCategory; // Danh mục cha

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<CategoryModel> childCategory; // Danh mục con

    @Column(name = "category_level")
    private Integer categoryLevel;//1,2,3 (min = 3), sản phẩm chỉ có thể gán category level = 3
}

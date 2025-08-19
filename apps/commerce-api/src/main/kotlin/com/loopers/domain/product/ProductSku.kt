package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Type

@Table(name = "product_sku")
@Entity
class ProductSku(
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,
    val skuCode: String,
    @Type(JsonType::class)
    @Column(columnDefinition = "json")
    val attributes: ProductSkuAttributes,
    val price: Money,
    @Enumerated(EnumType.STRING)
    val status: ProductSkuStatus,
) : BaseEntity() {

    enum class ProductSkuStatus {
        ACTIVE,
        INACTIVE,
        OUT_OF_STOCK,
    }

    data class ProductSkuAttributes(
        val color: String? = null,
        val size: String? = null,
    )
}

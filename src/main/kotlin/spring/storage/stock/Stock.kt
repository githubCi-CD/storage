package spring.storage.stock

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import spring.storage.origin.Origin

@Table("stock")
data class Stock(
    @Id
    val id: Long? = null,
    @JsonIgnore
    val originId: Long? = 0,
    val count: Long = 0,
    val factoryId: Long = 0,
    @Transient
    @Value("null")
    val origin: Origin? = null
) {
    companion object {
        fun fromKafkaDto(dto: StockConsumeDto): Stock =
            Stock(
                originId = dto.originId,
                count = dto.count,
                factoryId = dto.factoryId
            )
    }

    fun combine(from: Stock): Stock =
        this.copy(
            count = this.count + from.count
        )
}
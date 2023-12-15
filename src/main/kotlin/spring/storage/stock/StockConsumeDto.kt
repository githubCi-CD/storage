package spring.storage.stock

data class StockConsumeDto (
    val originId: Long?,
    val count: Long,
    val factoryId: Long
)
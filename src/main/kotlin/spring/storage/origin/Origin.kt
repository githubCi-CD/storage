package spring.storage.origin

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("origin")
data class Origin (
    @Id
    val id: Long? = null,
    val name: String? = null,
    val price: Long = 0
)
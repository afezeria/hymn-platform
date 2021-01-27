package github.afezeria.hymn.common.conf

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * @author afezeria
 */
@Configuration
@EnableConfigurationProperties(DataBaseProperties::class)
class DataBaseConfiguration {
    @Autowired
    lateinit var dataBaseProperties: DataBaseProperties

    @Bean
    fun dataSourceList(): List<DataSource> {
        return dataBaseProperties.db.map { HikariDataSource(HikariConfig(it)) }
    }

    @Bean
    fun dataBaseList(dataSourceList: List<DataSource>): List<Database> {
        if (dataSourceList.isEmpty())
            throw Exception("缺少数据源配置")
        return dataSourceList.map { Database.connect(it) }
    }

}
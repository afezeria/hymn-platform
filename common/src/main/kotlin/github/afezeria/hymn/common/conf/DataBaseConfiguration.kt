package github.afezeria.hymn.common.conf

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import github.afezeria.hymn.common.platform.DatabaseServiceImpl
import github.afezeria.hymn.common.platform.DatabaseService
import mu.KLogging
import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
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
    companion object:KLogging()
    @Autowired
    lateinit var dataBaseProperties: DataBaseProperties

    @Bean
    fun dataSourceList(): List<DataSource> {
        return dataBaseProperties.admin.map { HikariDataSource(HikariConfig(it)) }
    }

    @Bean
    fun databaseService():DatabaseService{
        return DatabaseServiceImpl(dataBaseProperties)
    }

}
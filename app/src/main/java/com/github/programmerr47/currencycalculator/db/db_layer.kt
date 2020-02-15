package com.github.programmerr47.currencycalculator.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.programmerr47.currencycalculator.util.bigDecimal
import io.reactivex.Single
import java.math.BigDecimal

@Database(entities = [CurrencyEntity::class], version = 1)
@TypeConverters(BigDecimalConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencyentity")
    fun getAll(): Single<List<CurrencyEntity>>

    @Insert(onConflict = REPLACE)
    fun insertAll(currencies: List<CurrencyEntity>)
}

fun CurrencyDao.getAllAsMap() = getAll().map { it.associate { it.type to it.value } }

fun CurrencyDao.insertAll(currencyMap: Map<String, BigDecimal>) =
        insertAll(currencyMap.map { CurrencyEntity(it.key, it.value) })

@Entity
data class CurrencyEntity(
        @PrimaryKey val type: String,
        val value: BigDecimal
)

class BigDecimalConverters {
    @TypeConverter
    fun fromString(value: String) = bigDecimal(value)

    @TypeConverter
    fun toString(value: BigDecimal) = value.stripTrailingZeros().toString()
}

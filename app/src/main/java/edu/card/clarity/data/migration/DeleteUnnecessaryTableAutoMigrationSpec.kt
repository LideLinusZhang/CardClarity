package edu.card.clarity.data.migration

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable(tableName = "receipts")
class DeleteUnnecessaryTableAutoMigrationSpec : AutoMigrationSpec
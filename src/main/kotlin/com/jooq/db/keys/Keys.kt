/*
 * This file is generated by jOOQ.
 */
package com.jooq.db.keys


import com.jooq.db.tables.Book
import com.jooq.db.tables.records.BookRecord

import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val PK_T_BOOK: UniqueKey<BookRecord> = Internal.createUniqueKey(Book.BOOK, DSL.name("PK_T_BOOK"), arrayOf(Book.BOOK.ID), true)
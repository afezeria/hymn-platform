package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.util.execute
import java.io.File

/**
 * @author afezeria
 */
object SimpleOrmTestHelper {
    fun init() {
        val sql = """
drop schema if exists test_dao cascade;
create schema test_dao;
drop table if exists test_dao.test_table;
create table test_dao.test_table
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    inta         int,
    longa        bigint,
    doublea      double precision,
    decimala     decimal,
    boola        bool,
    lazya        text,
    role_id      text,
    role_name    text,
    org_id       text,
    org_name     text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamptz,
    modify_date  timestamptz
);
create unique index test_table_inta_uk on test_dao.test_table (inta);

drop table if exists test_dao.test_table_history;
create table test_dao.test_table_history
(
    operation    text,
    stamp        timestamptz,
    id           text,
    inta         int,
    longa        bigint,
    doublea      double precision,
    decimala     decimal,
    boola        bool,
    lazya        text,
    role_id      text,
    role_name    text,
    org_id       text,
    org_name     text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamptz,
    modify_date  timestamptz
);
create or replace function test_dao.test_table_history_trigger() returns trigger
    language plpgsql as
${'$'}${'$'}
begin
    if tg_op = 'INSERT' then
        insert into test_dao.test_table_history select 'i', now(), new.*;
    elseif tg_op = 'UPDATE' then
        insert into test_dao.test_table_history select 'u', now(), new.*;
    elseif tg_op = 'DELETE' then
        insert into test_dao.test_table_history select 'd', now(), old.*;
    end if;
    return null;
end
${'$'}${'$'};
drop trigger if exists test_table_history_trigger on test_dao.test_table;
create trigger test_table_history_trigger
    after update or insert or delete
    on test_dao.test_table
    for each row
execute function test_dao.test_table_history_trigger();
        """
        val temp = File.createTempFile("dao-test-sql", ".sql")
        temp.deleteOnExit()
        temp.outputStream().use {
            it.write(sql.toByteArray())
        }
        BaseDbTest.runSqlScript(temp.absolutePath)
    }

    fun clear() {
        adminConn.use {
            it.execute("drop schema if exists test_dao cascade;")
        }

    }
}
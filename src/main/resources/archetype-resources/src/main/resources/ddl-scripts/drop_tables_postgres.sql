DROP INDEX IF EXISTS event_journal_slice_idx;
DROP TABLE  IF EXISTS event_journal CASCADE;
DROP TABLE  IF EXISTS snapshot CASCADE;
DROP TABLE  IF EXISTS durable_state CASCADE;
DROP TABLE  IF EXISTS akka_projection_offset_store CASCADE;
DROP TABLE  IF EXISTS akka_projection_timestamp_offset_store CASCADE;
DROP TABLE  IF EXISTS akka_projection_management CASCADE;
DROP INDEX IF EXISTS "event_journal_ordering_idx";

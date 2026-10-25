package main

import (
	"database/sql"
	"fmt"
	"testing"

	_ "github.com/lib/pq"

	"github.com/stretchr/testify/assert"
	"rori.dev/lunchbox/ocr/testhelpers"
)

func TestDB(t *testing.T) {
	if testing.Short() {
		t.Skip("skipping integration test")
	}

	container := testhelpers.NewPostgresContainer(t)
	t.Cleanup(func() {
		container.Close(t)
	})

	db, err := sql.Open("postgres", container.ConnectionString(t))
	assert.NoError(t, err)
	t.Cleanup(func() {
		db.Close() //nolint:errcheck
	})

	t.Run("ping db", func(t *testing.T) {
		createdbStmt := `create database todos`
		_, err := db.Exec(createdbStmt)
		assert.NoError(t, err)

		// err = db.Ping()
		rows, err := db.Query(`SELECT datname FROM pg_catalog.pg_database`)
		assert.NoError(t, err)
		defer rows.Close() //nolint:errcheck
		for rows.Next() {
			var name string

			err = rows.Scan(&name)
			assert.NoError(t, err)

			fmt.Println(name)
		}

		// assert.Fail(t, "fail intentionally")
	})
}

package testhelpers

import (
	"context"
	"fmt"
	"testing"
	"time"

	"github.com/stretchr/testify/require"

	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/wait"
)

// PostgresContainer represents the postgres container type used in the module
type PostgresContainer struct {
	testcontainers.Container
}

func (db *PostgresContainer) Host(t *testing.T) string {
	ctx, cancel := context.WithTimeout(context.Background(), time.Minute)
	defer cancel()
	p, err := db.Container.Host(ctx)
	require.NoError(t, err)
	return p
}

func (db *PostgresContainer) Port(t *testing.T) uint16 {
	ctx, cancel := context.WithTimeout(context.Background(), time.Minute)
	defer cancel()
	p, err := db.Container.MappedPort(ctx, "5432") // nolint:staticcheck
	require.NoError(t, err)
	return p.Num()
}

func (db *PostgresContainer) ConnectionString(t *testing.T) string {
	return fmt.Sprintf("postgres://postgres:postgres@%s:%d/postgres?sslmode=disable", db.Host(t), db.Port(t))
}

func (db *PostgresContainer) Close(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Minute)
	defer cancel()
	require.NoError(t, db.Container.Terminate(ctx)) // nolint:staticcheck
}

// NewPostgresContainer creates an instance of a postgres container
func NewPostgresContainer(t *testing.T) *PostgresContainer {
	ctx, cancel := context.WithTimeout(context.Background(), time.Minute)
	defer cancel()
	req := testcontainers.ContainerRequest{
		Image:        "postgres:18-alpine",
		ExposedPorts: []string{"5432/tcp"},
		Cmd:          []string{"postgres", "-c", "fsync=off"},
		Env: map[string]string{
			"POSTGRES_USER":     "postgres",
			"POSTGRES_PASSWORD": "postgres",
			"POSTGRES_DB":       "postgres",
		},
		WaitingFor: wait.ForListeningPort("5432/tcp").WithStartupTimeout(1 * time.Minute),
	}
	postgres, err := testcontainers.GenericContainer(ctx, testcontainers.GenericContainerRequest{
		ContainerRequest: req,
		Started:          true,
	})
	require.NoError(t, err)

	return &PostgresContainer{
		Container: postgres,
	}
}

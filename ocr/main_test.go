package main

import (
	"fmt"
	"net/http"
	"net/http/httptest"
	"os"
	"path/filepath"
	"strings"
	"testing"
)

func TestPostUrlFormat(t *testing.T) {
	binDir := t.TempDir()
	if err := os.WriteFile(filepath.Join(binDir, "tesseract"), []byte("#!/bin/sh\nprintf '%s\\n' \"$@\"\n"), 0755); err != nil {
		t.Fatal(err)
	}
	t.Setenv("PATH", binDir+string(os.PathListSeparator)+os.Getenv("PATH"))

	imageServer := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		_, _ = w.Write([]byte("image"))
	}))
	defer imageServer.Close()

	for _, tc := range []struct {
		name       string
		format     string
		wantStatus int
		wantHTML   bool
	}{
		{name: "default", wantStatus: http.StatusOK},
		{name: "text", format: `,"format":"text"`, wantStatus: http.StatusOK},
		{name: "html", format: `,"format":"html"`, wantStatus: http.StatusOK, wantHTML: true},
		{name: "invalid", format: `,"format":"pdf"`, wantStatus: http.StatusBadRequest},
	} {
		t.Run(tc.name, func(t *testing.T) {
			body := fmt.Sprintf(`{"img_url":%q%s}`, imageServer.URL, tc.format)
			req := httptest.NewRequest(http.MethodPost, "/url", strings.NewReader(body))
			resp := httptest.NewRecorder()
			handleUrl(resp, req)
			if resp.Code != tc.wantStatus {
				t.Fatalf("status = %d, want %d; body: %s", resp.Code, tc.wantStatus, resp.Body.String())
			}
			if resp.Code != http.StatusOK {
				return
			}
			args := strings.Split(strings.TrimSpace(resp.Body.String()), "\n")
			if len(args) < 4 || args[0] != "-l" || args[1] != "eng+deu" || args[3] != "stdout" {
				t.Fatalf("unexpected tesseract arguments: %q", args)
			}
			if tc.wantHTML {
				if len(args) != 5 || args[4] != "hocr" {
					t.Fatalf("unexpected hocr arguments: %q", args)
				}
			} else if len(args) != 4 {
				t.Fatalf("unexpected text arguments: %q", args)
			}
		})
	}
}

package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"os/exec"
)

func main() {
	http.HandleFunc("/url", handleUrl)
	if err := http.ListenAndServe(getServerAddr(), nil); err != nil {
		log.Fatal(err)
	}
}

func getServerAddr() string {
	port := os.Getenv("PORT")
	if port == "" {
		port = "9292"
	}
	return fmt.Sprintf(":%v", port)
}

func handleUrl(w http.ResponseWriter, req *http.Request) {
	if req.URL.Path != "/url" {
		log.Printf("request rejected: path %q not found", req.URL.Path)
		http.Error(w, "404 not found.", http.StatusNotFound)
		return
	}
	switch req.Method {
	case http.MethodPost:
		handlePostUrl(w, req)
	default:
		log.Printf("request rejected: method %q not allowed for %s", req.Method, req.URL.Path)
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

type PostRequestBody struct {
	ImageUrl string `json:"img_url"`
	Format   string `json:"format"`
}

func handlePostUrl(w http.ResponseWriter, req *http.Request) {
	reqBody := &PostRequestBody{}
	if err := json.NewDecoder(req.Body).Decode(reqBody); err != nil {
		log.Printf("could not decode request body: %v", err)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	if reqBody.Format != "" && reqBody.Format != "text" && reqBody.Format != "html" {
		log.Printf("request rejected: invalid OCR format %q", reqBody.Format)
		http.Error(w, "format must be text or html", http.StatusBadRequest)
		return
	}
	text, err := ocr(reqBody.ImageUrl, reqBody.Format)
	if err != nil {
		log.Printf("OCR failed for %q: %v", reqBody.ImageUrl, err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if _, err := io.WriteString(w, text); err != nil {
		log.Printf("could not write OCR response for %q: %v", reqBody.ImageUrl, err)
	}
}

func ocr(imageUrl, format string) (string, error) {
	log.Printf("ocr for %s with format %s", imageUrl, format)

	imageFile, err := downloadImage(imageUrl)
	if err != nil {
		return "", fmt.Errorf("download image: %w", err)
	}
	defer func() {
		if err := os.Remove(imageFile); err != nil && !os.IsNotExist(err) {
			log.Printf("could not remove temporary image %q: %v", imageFile, err)
		}
	}()

	text, err := runTesseract(imageFile, format)
	if err != nil {
		return "", fmt.Errorf("run tesseract: %w", err)
	}

	return text, nil
}

func downloadImage(imageUrl string) (string, error) {
	imageFile, err := os.CreateTemp(os.TempDir(), "tesseract-")
	if err != nil {
		return "", fmt.Errorf("create temporary image: %w", err)
	}
	downloaded := false
	defer func() {
		if err := imageFile.Close(); err != nil {
			log.Printf("could not close temporary image %q: %v", imageFile.Name(), err)
		}
		if !downloaded {
			if err := os.Remove(imageFile.Name()); err != nil && !os.IsNotExist(err) {
				log.Printf("could not remove incomplete temporary image %q: %v", imageFile.Name(), err)
			}
		}
	}()

	req, err := http.NewRequest(http.MethodGet, imageUrl, nil)
	if err != nil {
		return "", fmt.Errorf("create request for %q: %w", imageUrl, err)
	}
	req.Header.Set("User-Agent", "Mozilla/5.0")
	req.Header.Set("Accept", "image/avif,image/webp,image/png,image/jpeg,*/*;q=0.8")

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return "", fmt.Errorf("GET %q: %w", imageUrl, err)
	}
	defer func() {
		if err := resp.Body.Close(); err != nil {
			log.Printf("could not close response body for %q: %v", imageUrl, err)
		}
	}()
	if resp.StatusCode < http.StatusOK || resp.StatusCode >= http.StatusMultipleChoices {
		return "", fmt.Errorf("GET %q: unexpected status %s", imageUrl, resp.Status)
	}

	_, err = io.Copy(imageFile, resp.Body)
	if err != nil {
		return "", fmt.Errorf("write response from %q to temporary image: %w", imageUrl, err)
	}

	downloaded = true
	return imageFile.Name(), nil
}

func runTesseract(imageFile, format string) (string, error) {
	args := []string{"-l", "eng+deu", imageFile, "stdout"}
	if format == "html" {
		args = append(args, "hocr")
	}
	cmd := exec.Command("tesseract", args...)
	var stderr bytes.Buffer
	cmd.Stderr = &stderr
	out, err := cmd.Output()
	if err != nil {
		return "", fmt.Errorf("command failed: %w; stderr: %s", err, stderr.String())
	}
	if stderr.Len() > 0 {
		log.Printf("tesseract warning: %s", stderr.String())
	}
	return string(out), nil
}

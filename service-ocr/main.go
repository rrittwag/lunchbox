package main

import (
	"encoding/json"
	"fmt"
	"github.com/google/uuid"
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
		http.Error(w, "404 not found.", http.StatusNotFound)
		return
	}
	switch req.Method {
	case http.MethodPost:
		handlePostUrl(w, req)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

type PostRequestBody struct {
	ImageUrl string `json:"img_url"`
}

func handlePostUrl(w http.ResponseWriter, req *http.Request) {
	reqBody := &PostRequestBody{}
	if err := json.NewDecoder(req.Body).Decode(reqBody); err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	text, err := ocr(reqBody.ImageUrl)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	_, _ = io.WriteString(w, text)
}

func ocr(imageUrl string) (string, error) {
	imageFile, err := downloadImage(imageUrl)
	if err != nil {
		return "", err
	}

	text, err := runTesseract(imageFile)
	if err != nil {
		return "", err
	}

	_ = os.Remove(imageFile)
	return text, nil
}

func downloadImage(imageUrl string) (string, error) {
	id, err := uuid.NewUUID()
	if err != nil {
		return "", err
	}
	imageFile := fmt.Sprintf("/tempfile/%v", id)

	out, err := os.Create(imageFile)
	if err != nil {
		return "", err
	}
	defer out.Close()

	resp, err := http.Get(imageUrl)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	_, err = io.Copy(out, resp.Body)
	if err != nil {
		return "", err
	}

	return imageFile, nil
}

func runTesseract(imageFile string) (string, error) {
	out, err := exec.Command("tesseract", "-l", "eng+deu", imageFile, "stdout").Output()
	if err != nil {
		return "", err
	}
	return string(out), nil
}

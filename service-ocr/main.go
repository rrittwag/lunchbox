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
	port := os.Getenv("PORT")
	if port == "" {
		port = "9292"
	}
	serverAddr := fmt.Sprintf(":%v", port)

	http.HandleFunc("/url", handleUrl)
	err := http.ListenAndServe(serverAddr, nil)
	if err != nil {
		log.Fatal(err)
	}
}

func handleUrl(w http.ResponseWriter, req *http.Request) {
	if req.URL.Path != "/url" {
		http.Error(w, "404 not found.", http.StatusNotFound)
		return
	}

	switch req.Method {
	case http.MethodGet:
		handleGetUrl(w, req)
	case http.MethodPost:
		handlePostUrl(w, req)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

func handleGetUrl(w http.ResponseWriter, req *http.Request) {
	imageUrl := req.URL.Query().Get("q")
	if imageUrl == "" {
		log.Println("param q needed")
		http.Error(w, "param q needed", http.StatusBadRequest)
		return
	}
	text, err := ocr(imageUrl)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if _, err := io.WriteString(w, text); err != nil {
		return
	}
}

type PostRequestBody struct {
	ImageUrl string `json:"img_url"`
}

func handlePostUrl(w http.ResponseWriter, req *http.Request) {
	reqBody := &PostRequestBody{}
	err := json.NewDecoder(req.Body).Decode(reqBody)
	if err != nil {
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
	if _, err := io.WriteString(w, text); err != nil {
		return
	}
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
	imageFile := fmt.Sprintf("/tempocr/%v", id)

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

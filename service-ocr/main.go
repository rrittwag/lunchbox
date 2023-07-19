package main

import (
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
	err := http.ListenAndServe(":8080", nil)
	if err != nil {
		log.Fatal(err)
	}
}

func handleUrl(w http.ResponseWriter, req *http.Request) {
	imageUrl := req.URL.Query().Get("q")
	if imageUrl == "" {
		log.Println("param q needed")
		w.WriteHeader(http.StatusBadRequest)
		if _, err := io.WriteString(w, "param q needed"); err != nil {
			return
		}
		return
	}
	text, err := ocr(imageUrl)
	if err != nil {
		log.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		if _, err := io.WriteString(w, err.Error()); err != nil {
			return
		}
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

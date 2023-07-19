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
	imageUrl := "https://jeroen.github.io/images/testocr.png"
	text, err := bla(imageUrl)
	if err != nil {
		log.Fatal(err)
	}
	log.Println(text)
}

func bla(imageUrl string) (string, error) {
	imageFile, err := downloadImage(imageUrl)
	if err != nil {
		return "", err
	}
	textFile, err := runOcr(imageFile)
	if err != nil {
		return "", err
	}
	text, err := readText(textFile)
	if err != nil {
		return "", err
	}
	return text, nil
}

func downloadImage(imageUrl string) (string, error) {
	id, err := uuid.NewUUID()
	if err != nil {
		return "", err
	}
	imageFile := fmt.Sprintf("/tempocr/%v", id)

	out, err := os.Create(imageFile)
	defer out.Close()

	resp, err := http.Get(imageUrl)
	defer resp.Body.Close()

	_, err = io.Copy(out, resp.Body)
	if err != nil {
		return "", err
	}

	return imageFile, nil
}

func runOcr(imageFile string) (string, error) {
	cmd := exec.Command("tesseract", "-l", "deu", imageFile, imageFile)
	err := cmd.Run()
	return fmt.Sprintf("%v.txt", imageFile), err
}

func readText(textFile string) (string, error) {
	content, err := os.ReadFile(textFile)
	if err != nil {
		return "", err
	}
	return string(content), nil
}

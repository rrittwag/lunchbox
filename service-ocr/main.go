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
	imageUrl := "https://www.feldkuechebkarow.de/s/cc_images/teaserbox_25241614.jpg?t=1689675118"
	// imageUrl := "https://jeroen.github.io/images/testocr.png"
	text, err := bla(imageUrl)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(text)
}

func bla(imageUrl string) (string, error) {
	imageFile, err := downloadImage(imageUrl)
	if err != nil {
		return "", err
	}
	text, err := runOcr(imageFile)
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
	out, err := exec.Command("tesseract", "-l", "deu", imageFile, "stdout").Output()
	if err != nil {
		return "", err
	}
	return string(out), nil
}

package main

import (
	"fmt"
	"log"
	"net/http"
	"net/http/httputil"
	"time"
)

func demo(w http.ResponseWriter, r *http.Request) {
	val := `
   _                                   _                 _     _                                   
  | | ___ _ __   _____   _____     ___| | ___  _   _  __| |   (_) ___  _   _ _ __ _ __   ___ _   _ 
  | |/ _ \ '_ \ / _ \ \ / / _ \   / __| |/ _ \| | | |/ _  |   | |/ _ \| | | | '__| '_ \ / _ \ | | |
  | |  __/ | | | (_) \ V / (_) | | (__| | (_) | |_| | (_| |   | | (_) | |_| | |  | | | |  __/ |_| |
  |_|\___|_| |_|\___/ \_/ \___/   \___|_|\___/ \__,_|\__,_|  _/ |\___/ \__,_|_|  |_| |_|\___|\__, |
                                                            |__/                             |___/ 
 
`
	w.WriteHeader(http.StatusOK)
	fmt.Fprint(w, val)
}

func health(w http.ResponseWriter, r *http.Request) {
	// Simulate at least a bit of processing time.
	time.Sleep(100 * time.Millisecond)

	w.WriteHeader(http.StatusOK)
	if reqBytes, err := httputil.DumpRequest(r, true); err == nil {
		log.Printf("Openshift Http Request Dumper received a message: %+v", string(reqBytes))
		w.Write(reqBytes)
	} else {
		log.Printf("Error dumping the request: %+v :: %+v", err, r)
	}
}

func main() {
	m := http.NewServeMux()
	m.HandleFunc("/", demo)
	m.HandleFunc("/health", health)

	http.ListenAndServe(":8080", m)
}

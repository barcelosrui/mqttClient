---
# Instituto Politécnico de Leiria - 2020
# Mestrado de Cibersegurança e Informática Forense
# Segurança em Redes de Computadores
# 
---
---
## Alunos
	2190368 - Nuno Rodrigues
	2190230 - Rui Barcelos	
	
## Ficheiro de apoio ao trabalho prático da cadeira

### Gerar os certificados necessários para obtermos uma ligação com TLS entre o cliente e o broker
*  Criar um certificado digital para a CA
###	
	openssl req -newkey rsa:2048 -x509 -nodes -sha256 -days 365
	-extensions v3_ca -keyout ca.key -out ca.crt -subj /CN=localhost
* Criar um key pair para o broker MQTT:
###	
	openssl genrsa -out localhost.key 2048
* Requisitar um de certificado para o broker
###	
	openssl req -new -sha256 -out localhost.csr -key localhost.key 
	-subj /CN=localhost/O=ipl/OU=ipl/emailAddress=ipl@ipl.net
* Usar a CA key, para assinar e verificar este certificado
###
	openssl x509 -req -sha256 -in localhost.csr -CA ca.crt -CAkey ca.key
	-CAcreateserial -CAserial ca.srl -out localhost.crt -days 365

### Configurar o broker com TLS
* Adicionar os certificados à configuração do mosquitto (centOS)
###
	sudo nano /etc/mosquitto/mosquitto.conf

* Adicionar ao ficheiro
###
	cafile   /etc/mosquitto/ca_certificates/ca.crt
	certfile /etc/mosquitto/certs/localhost.crt
	keyfile  /etc/mosquitto/certs/localhost.key
	port 8883
	
* Criar as pastas e copiar os certificados
###
	cd /etc/mosquitto/
	sudo mkdir ca_certificates
	sudo mkdir certs
Na pasta onde se encontram os certificados
###
	sudo cp ca.crt /etc/mosquitto/ca_certificates
	sudo cp localhost.crt /etc/mosquitto/certs
	sudo cp localhost.key /etc/mosquitto/certs
Reiniciar o mosquitto para a nova configuração
###
	sudo system restart mosquitto
###
### Configurar a ESP8266 para TLS
Para termos TLS na ESP é necessário importar os vários certificados, para isso deve criar uma pasta com o nome "data" na pasta principal do projeto e copiar os certificados para essa pasta.
De seguida verificar se o Arduino IDE contém a ferramenta ESP8266 Sketch Data Updater, se existir, clicar e os certificados vão ser enviados para a flash.
Se ocorrer um error de upload para a flash, deve-se alterar no IDE no menu "Ferramentas", "Flash Size" e escolher 4M (3M SPIFFS).

* Se a ferramenta não existir, na pasta dos projetos do Arduino, criar a pasta "tools/ESP8266FS/tool" e adicionar o ficheiro esp8266fs.jar
* Descarregar a ferramenta: [ESP8266FS](https://github.com/esp8266/arduino-esp8266fs-plugin/releases/download/0.5.0/ESP8266FS-0.5.0.zip)

### Comando para ler as mensagem com o TLS	
É necessario adicionar o caminho correto para o local do ca.crt na pasta /etc/mosquitto/ca_certificates

	mosquitto_sub -t alert -v --cafile ca.crt
###	

---

	
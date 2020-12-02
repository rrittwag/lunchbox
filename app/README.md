# Lunchbox app
Ermittelt Mittagsangebote aus der Region.

## App starten
Die App startet über Docker Compose. Es benötigt 2 Umgebungsvariablen.

Das docker-compose-File enthält eine Variable BASE_DOMAIN zur Beschreibung des aktuellen Domainnamens (z.B. todo.example.com). Die Variable muss entweder in einer Datei .env hinterlegt sein oder als reine Environment-Variable vorliegen:

    export BASE_DOMAIN=example.com

Das docker-compose-File enthält außerdem eine Variable DOMAIN_PROTOCOL zur Definition des zu verwendenden Protokolls (z.B. https). Die Variable muss entweder in einer Datei .env hinterlegt sein oder als reine Environment-Variable vorliegen:

    export DOMAIN_PROTOCOL=http

Danach startet man Docker Compose via

    docker-compose up -d

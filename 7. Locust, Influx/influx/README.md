# Vremenske baze podataka (Time-Series Databases)

Pojedini zahtevi veb aplikacija se tiču napredne analitike i praćenja stanja sistema kroz vreme. Možemo uzeti primer pametne kuće i senzora koji šalju očitavanja temperature. Korisnicima je ove podatke potrebno prikazati u vidu linijskih grafika (kako temperatura varira u jedinici vremena), ali i kroz druge vizuelizacije poput stubičastih grafika (_bar plots_).

## Zašto relacione baze nisu dovoljne?

Čuvanje velikog broja istorijskih podataka i merenja u tradicionalnim relacionim bazama podataka često nije optimalno iz nekoliko razloga:
* **Obim upisa:** Podaci stižu konstantno i u velikom broju, što može zagušiti i prepuniti standardne tabele.
* **Veličina indeksa:** Vremenom indeksi postaju preveliki, što usporava pretragu po vremenskim opsezima.
* **Upravljanje životnim ciklusom podataka:** Relacione baze nemaju ugrađene mehanizme za automatsko agregiranje ili brisanje starih podataka koji više nisu potrebni.

Stoga se javlja potreba za korišćenjem **vremenskih baza podataka**, koje su projektovane za skladištenje, efikasno indeksiranje i brze analitičke upite nad vremenskim serijama.

Vremenske baze podataka skladišture podatke u vidu **tačaka (_data points_)**, pri čemu svaka tačka obavezno sadrži:
1. **Vremenski žig (_Timestamp_):** Tačan trenutak kada je merenje izvršeno.
2. **Vrednost (_Value_):** Numerička veličina koja se meri (obično realan broj).
3. **Oznake (_Tags / Metadata_):** Ključ-vrednost parovi koji bliže opisuju merenje (npr. `lokacija: zgrada_A`, `senzor: sprat_2`), što olakšava filtriranje i grupisanje.

## InfluxDB

_InfluxDB_ je jedna od najpopularnijih i najcesce koriscenih vremenskih baza podataka na trzistu. Dizajnirana je da podrzi visoke performanse upisa, efikasnu kompresiju i brze analiticke upite nad ogromnim kolicinama vremenish serija.

InfluxDB za koncept tacaka, koji reprezentuje skladistene podatke, vezuje nekoliko pojmova:

- **Measurement** - Logicki naziv skupa merenja (npr. `temperature`). Ovo je najblizi ekvivalent pojma "tabela" u relacionim bazama podataka.

- **Tags**: Oznake, koje mogu biti proizvoljni parovi kljuc-vrednost. Oznake se automatksi indeksiraju radi brzih pretraga, filtriranja i grupisanja podataka.

- **Fields**: Polja koja reprezentuju stvarne vrednosti koje se mere i cuvaju. Polja se **ne indeksiraju**, jer se obicno koriste za matematicke operacije i agregaciju.

- **Timestamp**: Precizan trenutak kada je podatak zabelezen. Ukoliko ne biva eksplicitno poslat, baza automatski upisuje trenutno vreme servera.

Razlicite verzije _InfluxDB_ koriste razlicite jezike za upite. Verzija u ovom primeru, odnosno verzija v2, koristi _Flux_ jezik za upite.

_Flux_ za razliku od _SQL-a_ koji koristi deklarativni pristup, koristi koncept cevovoda (_pipeline_) - podaci prolaze kroz niz koraka, gde se u svakom koraku transformišu, filtriraju ili agregiraju. Kao ključni operator koristi se `|>`, koji rezultat prethodne funkcije prosleđuje kao ulaz u sledeću.

Tipičan Flux upit se sastoji od nekoliko logičkih koraka u nizu:

- **Definisanje izvora (from)**: Određuje iz kog "baketa" (_bucket_) se povlače podaci.

- **Vremenski opseg (range)**: Definiše vremenski prozor od kog do kog trenutka se podaci pretražuju (npr. poslednjih sat vremena, poslednji dan).

- **Filtriranje (filter)**: Sužava izbor podataka na osnovu merenja (_measurement_), tagova (lokacija, senzori) ili polja. Koristi anonimnu funkciju oblika (r) => ... gde r predstavlja red podataka.

- **Agregacija i transformacija (aggregateWindow ili funkcije poput mean, sum)**: Grupiše podatke u vremenske intervale (npr. na svakih 5 minuta) i primenjuje matematičku funkciju nad njima.

- **Ispis rezultata (yield)**: Vraća konačan skup podataka krajnjem klijentu ili vizuelizaciji.

Odnosno, sklopljeno u jedan _Flux_ upit:

```bash
from(bucket: "pametna_kuca")
  |> range(start: -24h)
  |> filter(fn: (r) => r["_measurement"] == "temperature")
  |> filter(fn: (r) => r["_field"] == "value")
  |> filter(fn: (r) => r["lokacija"] == "zgrada_A")
  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)
  |> yield(name: "mean")
```

## Grafana

_Grafana_ je otvorena platforma za vizuelizaciju i analitiku podataka koja se u praksi najčešće kombinuje sa vremenskim bazama poput _InfluxDB-a_. Ona omogućava da sirove podatke i složene upite pretvorite u interaktivne, pregledne i vizuelno privlačne komandne table (_dashboards_).

Osnovni pojmovi u Grafani:
- **Izvor podataka (_Data Source_)**: Konfigurabilna veza između Grafane i baze podataka (u našem slučaju InfluxDB). Grafana se povezuje sa bazom i omogućava izvršavanje upita direktno iz svog korisničkog interfejsa.

- **Komandna tabla (_Dashboard_)**: Centralna stranica koja sadrži skup povezanih vizuelnih prikaza i pruža sveobuhvatan uvid u stanje sistema u realnom vremenu.

- **Panel**: Pojedinačni vizuelni element unutar komandne table. Svaki panel koristi upit (poput našeg _Flux_ upita) da povuče podatke iz baze i prikaže ih u željenoj formi — bilo kao linijski grafikon _(line chart_), stubičasti dijagram (_bar chart_), brojčani indikator (_stat_), ili tabelu.

U radu sa _InfluxDB_, _Flux_ upiti se unose direktno u editor panela unutar _Grafane_. _Grafana_ automatski prepoznaje vremensku osu (**_time**) i vrednosti iz rezultata upita, omogućavajući krajnjim korisnicima da dinamicki menjaju opseg prikaza.

## Primer

### Setup

Da biste pokrenuli primer u ovim vezbama, trebate imati instaliran Docker, Java programski jezik, NodeJS i Angular CLI.

Prvo je neophodno da pokrenete sve servise, sadrzane u `docker-compose.yml` fajlu. Ovo cete uraditi putem komande:
```bash
docker compose up
# ili da ne blokirate sebi ternimal
docker compose up -d
```

Nakon sto se slike kontejnera poskidaju iz registra, i kontejneri pokrenu mozete pristupiti servisima _InfluxDB_ i _Grafana_ putem sledecih adresa koje cete otkucati u pretrazivacu:

```bash
# Influx
http://localhost:8086
# Grafana
http://localhost:3000
```

Kredencijali za login na admin panele oba servisa se nalaze unutar `docker-compose.yml`.

Da biste pokrenuli frontend aplikaciju, navigirajte se u folder `influx_front`. Ovde cete pokrenuti naredne komande u svom terminalu:

```bash
# Instalacija svih zavisnosti aplikacije
npm install
# Serviranje aplikacije na adresi http://localhost:4200
ng serve --open
```

Neophodno je jos da pokrenete serversku aplikaciju koja je napravljena u `Spring Boot`-u unutar svog razvojnog okruzenja. Ova aplikacija nakon pokretanja ce slusati na portu `8080`.

### Demonstracija

Na frontend aplikaciji imate prikazane tabelarne, graficke i _Grafana_ prikaze. Mozete promeniti u drop-down listi koji interval zelite da gledate.

Bitno je uociti da se selektovani interval salje serverskoj aplikaciju u slucaju tabelarnog i grafickog prikaza. Serverska aplikacija vrsi upit u InfluxDB, nakon cega rezultat vraca frontend aplikaciji.

Kod _Grafana_ prikaza, vrsi se upit direktno _Grafana_ servisu (prikaz je omogucen putem ugradjenih panela i `<iframe>` taga.)

Unutar foldera grafana-settings možete analizirati strukturu fajlova koji se tu nalaze. Ovi fajlovi se, zajedno sa `docker-compose.yml`, koriste kako bi se omogućilo automatsko podešavanje i perzistiranje kontrolnih tabli i panela. Ovaj pristup, takozvani provisioning, vam omogućava da imate konzistentan prikaz i podešavanja, nezavisno od mašine sa koje pokrećete servis.
/**
 * Die Mittagsangebote ermittelt ein HTTP POST an
 * https://dev.hansenundtoth.de/fleischerhut/api/module_frontend/v/2.3/public_get/deals
 * mit JSON-Content
 * ```json
 * {
 *   "kid": "62",
 *   "branch": "133",
 *   "cat": "4558",
 *   "deliver": "1"
 * }
 * ```
 * Das Resultat ist vom Typ `ApiResult`.
 */

type Product = {
  idProduct: string
  name: string
  description: string
  price: string // Preis in Notation "7.9" für 7,90 €
  original_price: string // Jedes Mittagsangebot erkennt man daran, dass Preis und Originalpreis gleich sind.
}

type Weeks = {
   f: string, // Montag als ISO-konformes Datum
   p: Product[]
}

export type ApiResult = {
  Data: {
    data: Weeks[]
  }
}

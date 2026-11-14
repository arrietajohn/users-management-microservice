#!/usr/bin/env bash
# Mide el tiempo de respuesta de GET /api/users (cada llamada consulta la base de datos).
#
# Uso:   scripts/medir-latencia.sh [URL_BASE] [NUMERO_DE_LLAMADAS]
# Ej.:   scripts/medir-latencia.sh http://localhost:8080 50
#
# Credenciales del login: ADMIN_EMAIL y ADMIN_PASSWORD (por defecto, el admin inicial de schema.sql).
set -euo pipefail

BASE_URL="${1:-http://localhost:8080}"
CALLS="${2:-50}"
ADMIN_EMAIL="${ADMIN_EMAIL:-admin@example.com}"
ADMIN_PASSWORD="${ADMIN_PASSWORD:-Admin1234!}"

TOKEN=$(curl -sf -X POST "$BASE_URL/api/auth/login" \
  -H 'Content-Type: application/json' \
  -d "{\"email\":\"$ADMIN_EMAIL\",\"password\":\"$ADMIN_PASSWORD\"}" \
  | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')

# Una llamada de calentamiento: la primera abre conexiones y no representa el uso normal.
curl -sf -o /dev/null -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/users"

for _ in $(seq 1 "$CALLS"); do
  curl -sf -o /dev/null -w '%{time_total}\n' -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/users"
done | sort -n | awk -v url="$BASE_URL" '
  { t[NR] = $1 * 1000; sum += t[NR] }
  END {
    p50 = t[int(NR * 0.50 + 0.5)]; p95 = t[int(NR * 0.95 + 0.5)]
    printf "%s  llamadas=%d  promedio=%.1f ms  p50=%.1f ms  p95=%.1f ms  max=%.1f ms\n",
      url, NR, sum / NR, p50, p95, t[NR]
  }'

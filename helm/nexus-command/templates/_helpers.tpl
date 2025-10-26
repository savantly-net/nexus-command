{{/*
Expand the name of the chart.
*/}}
{{- define "nexus-command.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "nexus-command.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "nexus-command.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "nexus-command.labels" -}}
helm.sh/chart: {{ include "nexus-command.chart" . }}
{{ include "nexus-command.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "nexus-command.selectorLabels" -}}
app.kubernetes.io/name: {{ include "nexus-command.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "nexus-command.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "nexus-command.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Generate the database URL based on configuration
*/}}
{{- define "nexus-command.databaseUrl" -}}
{{- if .Values.postgresql.enabled }}
{{- printf "jdbc:postgresql://%s-postgresql:5432/%s" (include "nexus-command.fullname" .) .Values.postgresql.auth.database }}
{{- else }}
{{- if .Values.secrets.database.url }}
{{- .Values.secrets.database.url }}
{{- else }}
{{- printf "jdbc:postgresql://%s:%v/%s%s" .Values.externalDatabase.host (.Values.externalDatabase.port | int) .Values.externalDatabase.database .Values.externalDatabase.jdbcParams }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Generate the database username based on configuration
*/}}
{{- define "nexus-command.databaseUsername" -}}
{{- if .Values.postgresql.enabled }}
{{- .Values.postgresql.auth.username }}
{{- else }}
{{- if .Values.secrets.database.username }}
{{- .Values.secrets.database.username }}
{{- else }}
{{- .Values.externalDatabase.username }}
{{- end }}
{{- end }}
{{- end }}

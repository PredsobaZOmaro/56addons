param(
	[string]$InputDir = "src/main/resources/assets/addons56/sounds",
	[switch]$Overwrite,
	[switch]$DeleteSource,
	[int]$Quality = 4
)

$ErrorActionPreference = "Stop"

$ffmpeg = Get-Command ffmpeg -ErrorAction SilentlyContinue
if (-not $ffmpeg) {
	Write-Error "ffmpeg was not found in PATH. Install ffmpeg and run this script again."
}

if (-not (Test-Path -LiteralPath $InputDir)) {
	Write-Error "Input directory not found: $InputDir"
}

$mp3Files = Get-ChildItem -Path $InputDir -Filter *.mp3 -File
if ($mp3Files.Count -eq 0) {
	Write-Host "No .mp3 files found in $InputDir"
	exit 0
}

$created = 0
$skipped = 0
$failed = 0

foreach ($mp3 in $mp3Files) {
	$oggPath = Join-Path $mp3.DirectoryName ($mp3.BaseName + ".ogg")

	if ((Test-Path -LiteralPath $oggPath) -and -not $Overwrite) {
		Write-Host "Skip (already exists): $($mp3.Name) -> $([System.IO.Path]::GetFileName($oggPath))"
		$skipped++
		continue
	}

	& $ffmpeg.Source -y -i $mp3.FullName -c:a libvorbis -q:a $Quality $oggPath | Out-Null
	if ($LASTEXITCODE -ne 0) {
		Write-Warning "Failed: $($mp3.Name)"
		$failed++
		continue
	}

	if ($DeleteSource) {
		Remove-Item -LiteralPath $mp3.FullName -Force
	}

	Write-Host "Converted: $($mp3.Name) -> $([System.IO.Path]::GetFileName($oggPath))"
	$created++
}

Write-Host ""
Write-Host "Done. Converted: $created, Skipped: $skipped, Failed: $failed"

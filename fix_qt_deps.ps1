$ErrorActionPreference = "Continue"
$projectDir = "C:\Users\Rajaram\Desktop\Auth_ember\auth-ember\node_modules"

# Find all quick-temp directories
$qtDirPaths = Get-ChildItem $projectDir -Recurse -Filter "package.json" -ErrorAction SilentlyContinue | Where-Object {
    $c = Get-Content $_.FullName -Raw -ErrorAction SilentlyContinue
    $c -match '"name":\s*"quick-temp"'
} | ForEach-Object { $_.Directory.FullName }

Write-Host "Found $($qtDirPaths.Count) quick-temp instances"

$fixed = 0
foreach ($dir in $qtDirPaths) {
    Write-Host "Installing deps in: $($dir.Substring(60))..."
    $result = cmd /c "cd /d ""$dir"" & npm install 2>&1"
    if ($LASTEXITCODE -eq 0) {
        $fixed++
    } else {
        Write-Host "  FAILED: $($result | Select-Object -Last 1)"
    }
}

Write-Host "`nFixed: $fixed of $($qtDirPaths.Count)"

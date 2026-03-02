$srcQt = "$env:TEMP\qt_fresh\node_modules\quick-temp"
$projectDir = "C:\Users\Rajaram\Desktop\Auth_ember\auth-ember\node_modules"

$qtDirPaths = Get-ChildItem $projectDir -Recurse -Filter "package.json" -ErrorAction SilentlyContinue | Where-Object {
    $c = Get-Content $_.FullName -Raw -ErrorAction SilentlyContinue
    $c -match '"name":\s*"quick-temp"'
} | ForEach-Object { $_.Directory.FullName }

Write-Host "Found $($qtDirPaths.Count) quick-temp instances"

$replaced = 0
$failed = 0
foreach ($dir in $qtDirPaths) {
    try {
        Remove-Item $dir -Recurse -Force -ErrorAction Stop
        Copy-Item $srcQt $dir -Recurse -Force -ErrorAction Stop
        $replaced++
    } catch {
        Write-Host "FAILED: $dir - $_"
        $failed++
    }
}
Write-Host "Replaced: $replaced, Failed: $failed"

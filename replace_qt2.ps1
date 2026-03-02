$ErrorActionPreference = "Continue"
$srcQt = "$env:TEMP\qt_fresh\node_modules\quick-temp"
$projectDir = "C:\Users\Rajaram\Desktop\Auth_ember\auth-ember\node_modules"

# Find all quick-temp directories
$qtDirPaths = Get-ChildItem $projectDir -Recurse -Filter "package.json" -ErrorAction SilentlyContinue | Where-Object {
    $c = Get-Content $_.FullName -Raw -ErrorAction SilentlyContinue
    $c -match '"name":\s*"quick-temp"'
} | ForEach-Object { $_.Directory.FullName }

Write-Host "Found $($qtDirPaths.Count) quick-temp instances"

# Use cmd's rmdir which handles long paths better
$replaced = 0
$failed = 0
foreach ($dir in $qtDirPaths) {
    # Use robocopy to mirror an empty dir (handles long paths), then remove
    $emptyDir = "$env:TEMP\empty_dir_for_cleanup"
    if (-not (Test-Path $emptyDir)) { New-Item -ItemType Directory $emptyDir | Out-Null }
    
    # Robocopy /MIR mirrors empty dir to target, effectively deleting all contents
    $null = cmd /c "robocopy ""$emptyDir"" ""$dir"" /MIR /R:1 /W:0 >nul 2>&1"
    $null = cmd /c "rmdir /s /q ""$dir"" 2>nul"
    
    if (Test-Path $dir) {
        Write-Host "FAILED to remove: $dir"
        $failed++
    } else {
        Copy-Item $srcQt $dir -Recurse -Force -ErrorAction SilentlyContinue
        if (Test-Path "$dir\index.js") {
            $replaced++
        } else {
            Write-Host "FAILED to copy: $dir"
            $failed++
        }
    }
}

# Clean up empty dir
Remove-Item "$env:TEMP\empty_dir_for_cleanup" -Force -ErrorAction SilentlyContinue

Write-Host "`nReplaced: $replaced, Failed: $failed"

$ErrorActionPreference = "Continue"
$srcQt = "$env:TEMP\qt_fresh\node_modules\quick-temp"
$projectDir = "C:\Users\Rajaram\Desktop\Auth_ember\auth-ember\node_modules"

# Find all quick-temp directories
$qtDirPaths = Get-ChildItem $projectDir -Recurse -Filter "package.json" -ErrorAction SilentlyContinue | Where-Object {
    $c = Get-Content $_.FullName -Raw -ErrorAction SilentlyContinue
    $c -match '"name":\s*"quick-temp"'
} | ForEach-Object { $_.Directory.FullName }

Write-Host "Found $($qtDirPaths.Count) quick-temp instances"

# First clean all, then copy using robocopy which handles long paths
$emptyDir = "$env:TEMP\empty_dir_cleanup"
if (-not (Test-Path $emptyDir)) { New-Item -ItemType Directory $emptyDir | Out-Null }

$replaced = 0
$failed = 0
foreach ($dir in $qtDirPaths) {
    # Clean using robocopy mirror
    $null = cmd /c "robocopy ""$emptyDir"" ""$dir"" /MIR /R:1 /W:0 >nul 2>&1"
    $null = cmd /c "rmdir /s /q ""$dir"" 2>nul"
    
    # Copy using robocopy which handles long paths
    $null = cmd /c "robocopy ""$srcQt"" ""$dir"" /E /R:1 /W:0 >nul 2>&1"
    
    if (Test-Path "$dir\index.js") {
        # Verify the helper files exist
        $helperCount = (Get-ChildItem "$dir\node_modules\underscore.string\helper" -ErrorAction SilentlyContinue | Measure-Object).Count
        if ($helperCount -ge 5) {
            $replaced++
        } else {
            Write-Host "INCOMPLETE COPY ($helperCount helpers): $($dir.Substring(60))"
            $failed++
        }
    } else {
        Write-Host "FAILED: $($dir.Substring(60))"
        $failed++
    }
}

Remove-Item $emptyDir -Force -ErrorAction SilentlyContinue
Write-Host "`nReplaced: $replaced, Failed: $failed"

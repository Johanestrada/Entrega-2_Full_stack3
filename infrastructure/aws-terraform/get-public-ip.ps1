$ErrorActionPreference = 'Stop'

try {
  $publicIp = (Invoke-RestMethod -Uri 'https://checkip.amazonaws.com' -TimeoutSec 10).Trim()
  $parsedIp = [System.Net.IPAddress]::Parse($publicIp)
  if ($parsedIp.AddressFamily -ne [System.Net.Sockets.AddressFamily]::InterNetwork) {
    throw 'The public IP endpoint did not return an IPv4 address.'
  }

  @{ ipv4 = $publicIp } | ConvertTo-Json -Compress
} catch {
  [Console]::Error.WriteLine("Unable to detect the public IPv4 address: $($_.Exception.Message)")
  exit 1
}

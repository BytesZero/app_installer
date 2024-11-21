#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'app_installer'
  s.version          = '1.2.1'
  s.summary          = 'App Installer'
  s.description      = 'App Installer'
  s.homepage         = 'https://github.com/BytesZero/app_installer'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Zero' => 'zhengsonglan001@gmail.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'

  s.ios.deployment_target = '8.0'
end


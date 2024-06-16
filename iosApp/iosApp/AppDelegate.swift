//
//  AppDelegate.swift
//  iosApp
//
//  Created by Rok Oblak on 16. 6. 24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import ComposeApp
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {
    
       let diComponent: AppComponent = AppComponentKt.createAppComponent()

       override init() {
           super.init()
       }
}

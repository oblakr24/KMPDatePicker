import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {

    let component: AppComponent
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(component: component)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {

    let component: AppComponent
    var body: some View {
        ComposeView(component: component)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}




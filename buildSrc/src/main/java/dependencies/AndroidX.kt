package dependencies

object AndroidX {

    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val sliding_panel_layout =
        "androidx.slidingpanelayout:slidingpanelayout:${Versions.sliding_panel_layout}"

    const val app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"
    const val recycler_view = "androidx.recyclerview:recyclerview:${Versions.recycler_view}"
    const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    const val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val ui_tooling = "androidx.ui:ui-tooling:${Versions.androidx_ui}"

    const val nav_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.nav_component}"
    const val nav_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.nav_component}"

    const val compose_ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val compose_foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val compose_material = "androidx.compose.material:material:${Versions.compose}"
    const val compose_icons_core =
        "androidx.compose.material:material-icons-core:${Versions.compose}"
    const val compose_icons_extended =
        "androidx.compose.material:material-icons-extended:${Versions.compose}"

    const val navigation_hilt = "androidx.hilt:hilt-navigation:${Versions.hilt_navigation}"


    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    const val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore}"

    const val hilt_lifecycle_viewmodel =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt_lifecycle_viewmodel}"
}
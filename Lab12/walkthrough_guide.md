# Hướng dẫn tạo 2 màn hình, Navigation Bar và kết nối bằng SharedViewModel

Ứng dụng này bao gồm 2 màn hình (Fragment) và sử dụng `BottomNavigationView` để chuyển đổi qua lại. Dữ liệu (số lần bấm nút) được truyền từ Màn hình 1 sang Màn hình 2 thông qua `SharedViewModel`.

Dưới đây là các bước chi tiết để thực hiện tính năng này.

## Bước 1: Khởi tạo File tài nguyên (XML)

### 1.1. Thêm các chuỗi văn bản (Strings)

Mở file `app/src/main/res/values/strings.xml` và thêm các dòng sau vào trong thẻ `<resources>`:

```xml
<resources>
    <string name="app_name">demo_fragment</string>
    <string name="screen_one">Screen 1</string>
    <string name="screen_two">Screen 2</string>
    <string name="click_me">Click Me!</string>
    <string name="count_0">Count: 0</string>
</resources>
```

### 1.2. Tạo Menu cho Navigation Bar

1. Chuột phải vào thư mục `app/src/main/res`, chọn **New -> Android Resource Directory**. Chọn Resource type là `menu` rồi ấn OK.
2. Chuột phải vào thư mục `menu` vừa tạo, chọn **New -> Menu Resource File**, đặt tên là `bottom_nav_menu.xml`.
3. Mở file `bottom_nav_menu.xml` và copy đoạn code sau vào:

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/nav_fragment_one"
        android:title="@string/screen_one" />
    <item
        android:id="@+id/nav_fragment_two"
        android:title="@string/screen_two" />
</menu>
```

## Bước 2: Thiết lập Giao diện (Layouts)

### 2.1. Giao diện chính (`activity_main.xml`)

Mở file `app/src/main/res/layout/activity_main.xml` và thay thế toàn bộ nội dung thành:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- Phần chứa các Fragment -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/fragment_container"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/bottom_navigation"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <!-- Thanh điều hướng ở dưới -->
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 2.2. Giao diện Màn hình 1 (`fragment_one.xml`)

1. Chuột phải vào `app/src/main/res/layout`, chọn **New -> Layout Resource File**, đặt tên là `fragment_one.xml`.
2. Thay thế nội dung bằng:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <Button
        android:id="@+id/btn_increment"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/click_me" />

</LinearLayout>
```

### 2.3. Giao diện Màn hình 2 (`fragment_two.xml`)

1. Chuột phải vào `app/src/main/res/layout`, chọn **New -> Layout Resource File**, đặt tên là `fragment_two.xml`.
2. Thay thế nội dung bằng:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tv_count"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/count_0"
        android:textSize="24sp" />

</LinearLayout>
```

## Bước 3: Viết Code Java

_Lưu ý: Các file Java dưới đây đều được tạo trong thư mục `app/src/main/java/com/daonq/demo_fragment/`_

### 3.1. Tạo SharedViewModel

1. Chuột phải vào package `com.daonq.demo_fragment`, chọn **New -> Java Class**, đặt tên là `SharedViewModel`.
2. Class này sẽ chứa biến đếm để cả 2 màn hình cùng truy cập. Copy đoạn sau:

```java
package com.daonq.demo_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    // Biến lưu trữ dữ liệu, khởi tạo bằng 0
    private final MutableLiveData<Integer> clickCount = new MutableLiveData<>(0);

    // Hàm gọi khi muốn tăng số đếm
    public void incrementCount() {
        if (clickCount.getValue() != null) {
            clickCount.setValue(clickCount.getValue() + 1);
        }
    }

    // Hàm gọi khi muốn lấy dữ liệu ra xem
    public LiveData<Integer> getClickCount() {
        return clickCount;
    }
}
```

### 3.2. Tạo FragmentOne

1. Chuột phải vào package `com.daonq.demo_fragment`, chọn **New -> Java Class**, đặt tên là `FragmentOne`.
2. Khi nhấn nút ở đây, ta sẽ gọi hàm `incrementCount()` của ViewModel. Copy đoạn sau:

```java
package com.daonq.demo_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FragmentOne extends Fragment {

    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện fragment_one.xml
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // CỰC KỲ QUAN TRỌNG: Lấy SharedViewModel được gắn với Activity chứa nó
        // (requireActivity()) để xài chung 1 bản sao duy nhất giữa các Fragment
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Button btnIncrement = view.findViewById(R.id.btn_increment);

        // Lắng nghe sự kiện click button
        btnIncrement.setOnClickListener(v -> {
            // Gọi hàm tăng số đếm trong ViewModel
            sharedViewModel.incrementCount();
        });
    }
}
```

### 3.3. Tạo FragmentTwo

1. Tương tự, tạo class tên là `FragmentTwo`.
2. Màn hình này sẽ "lắng nghe" (observe) sự thay đổi của biến đếm. Cập nhật nội dung:

```java
package com.daonq.demo_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FragmentTwo extends Fragment {

    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện fragment_two.xml
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy SharedViewModel dùng chung
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        TextView tvCount = view.findViewById(R.id.tv_count);

        // Quan sát (observe) dữ liệu. Mỗi khi clickCount thay đổi, phương thức sẽ tự động chạy
        sharedViewModel.getClickCount().observe(getViewLifecycleOwner(), count -> {
            tvCount.setText("Count: " + count);
        });
    }
}
```

### 3.4. Cập nhật MainActivity

Mở file `MainActivity.java`, cập nhật lại code để xử lý logic khi bấm vào thanh điều hướng bên dưới, hệ thống sẽ chuyển đổi 2 Fragment.

```java
package com.daonq.demo_fragment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Căn chỉnh System UI (ẩn bớt viền dư thừa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Lắng nghe sự kiện người dùng bấm vào tab trên thanh điều hướng
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Dựa vào ID để biết người dùng chọn màn hình số mấy
            if (itemId == R.id.nav_fragment_one) {
                selectedFragment = new FragmentOne();
            } else if (itemId == R.id.nav_fragment_two) {
                selectedFragment = new FragmentTwo();
            }

            // Thay thế layout có sẵn (fragment_container) bằng Fragment mới tạo
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Bật màn hình 1 làm mặc định khi ứng dụng vừa mới mở lên (và chưa có khôi phục log)
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_fragment_one);
        }
    }
}
```

## Chạy thử ứng dụng

1. Bấm nút **Run** (mũi tên màu xanh lá) để khởi chạy ứng dụng trên máy ảo/điện thoại thật.
2. Ứng dụng sẽ hiển thị **Screen 1** cùng nút bấm.
3. Bấm vào nút `Click Me!` vài lần.
4. Nhấn giữ thanh Navigation phía dưới, chuyển sang **Screen 2**.
5. Bạn sẽ thấy dòng chữ cập nhật `Count ...` đúng bằng chính xác số lần bạn đã bấm ở Screen 1. Do dùng `SharedViewModel`, dữ liệu sẽ không bị mất khi bạn chuyển qua lại giữa 2 màn hình.

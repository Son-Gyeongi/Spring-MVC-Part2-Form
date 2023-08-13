package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /**
     * FormItemController에서 어떤 호출이든 자동으로 model에 regions가 들어간다.
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        // 멀티 데이터 넘겨주기
        // HashMap - 순서 보장 안됨, LinkedHashMap - 순서 보장 됨
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    } // model.addAttribute("regions", regions); 와 같다.

    // 라디오 버튼, 무조건 하나가 선택이 되어서 히든 필드가 없다.(null이 넘어오는 경우가 없다.)
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        // ItemType.values()를 하면 enum파일에 있는 값들을 배열로 넘겨준다.
//        ItemType[] values = ItemType.values();
//        return values;
        // 위 2줄 한줄로 합치기
        return ItemType.values();
    } // model.addAttribute("itemTypes", itemTypes); 와 같다.

    // 셀렉트 박스
    // DeliveryCode 라는 자바 객체를 사용하는 방법으로 진행
    // @ModelAttribute는 메서드 호출할 때마다 deliveryCodes()가 호출이 된다. 그러면 객체가 계속 생성될거다.
    // 예시는 단순하게 보여주기 위해 이러한 방식을 선택했지만 실제로는
    // 미리 생성한 후 생성한 걸 불러 쓰는게 더 효율적이다. 메모리를 생성했다가 지웠다 안해도 되서 더 효율적인 거다.
    // static식으로 만든 후에 공유해서 쓰는게 더 좋다.
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        // 멀티 데이터 넘겨주기
        // HashMap - 순서 보장 안됨, LinkedHashMap - 순서 보장 됨
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // 타임리프가 지원하는 폼 기능을 쓰려면 먼저 model을 하나 넘겨줘야 한다.
        // 빈 아이템이라도 넘겨줘야 한다.
        model.addAttribute("item", new Item());

        // 멀티 데이터 넘겨주기
        // HashMap - 순서 보장 안됨, LinkedHashMap - 순서 보장 됨
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        // open 값이 잘 넘어왔는지 확인
        log.info("item.open = {}", item.getOpen());
        // regions 값이 잘 들어갔는지 확인
        log.info("item.regions = {}", item.getRegions());
        // itemType 값이 잘 들어오는지 확인
        log.info("item.itemType = {}", item.getItemType());
        
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        // 멀티 데이터 넘겨주기
        // HashMap - 순서 보장 안됨, LinkedHashMap - 순서 보장 됨
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}


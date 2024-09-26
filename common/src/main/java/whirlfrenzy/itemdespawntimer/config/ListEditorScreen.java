package whirlfrenzy.itemdespawntimer.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class ListEditorScreen extends Screen {
    private Screen parentScreen;
    private TextFieldWidget itemIDField;
    private ButtonWidget addEntryButton;
    private ItemList list;

    protected ListEditorScreen(Text title) {
        super(title);
    }

    public ListEditorScreen(Screen parentScreen){
        this(Text.translatable("item-despawn-timer.midnightconfig.listEditor"));
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, -1);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("item-despawn-timer.midnightconfig.itemId"), this.width / 2 - 95, 24, -1);
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).dimensions(this.width / 2 - 75, this.height - 26, 150, 20).build());
        this.itemIDField = new TextFieldWidget(this.textRenderer, this.width / 2 - 95, 35, 165, 20, Text.translatable("item-despawn-timer.midnightconfig.itemId"));
        this.itemIDField.setTextPredicate(text -> {
            if(text.isEmpty()){
                this.itemIDField.setEditableColor(-1);
                this.itemIDField.setTooltip(null);

                return true;
            }

            Identifier id = Identifier.tryParse(text);
            if(id == null || !Registries.ITEM.containsId(id)){
                this.itemIDField.setEditableColor(0xFFFF7777);
                this.itemIDField.setTooltip(Tooltip.of(Text.translatable("item-despawn-timer.midnightconfig.invalidItem").formatted(Formatting.RED)));
            } else if(ItemDespawnTimerClientConfig.whitelistedItems.contains(id)){
                this.itemIDField.setEditableColor(0xFFFF7777);
                this.itemIDField.setTooltip(Tooltip.of(Text.translatable("item-despawn-timer.midnightconfig.itemAlreadyPresent").formatted(Formatting.RED)));
            } else {
                this.itemIDField.setEditableColor(-1);
                this.itemIDField.setTooltip(null);
            }

            return true;
        });

        this.itemIDField.setPlaceholder(Text.translatable("item-despawn-timer.midnightconfig.itemId.placeholder").formatted(Formatting.GRAY));

        this.addEntryButton = new ButtonWidget.Builder(Text.literal("+"), widget -> {
            Identifier id = Identifier.tryParse(this.itemIDField.getText());
            if(id == null || !Registries.ITEM.containsId(id) || ItemDespawnTimerClientConfig.whitelistedItems.contains(id)){
                return;
            }

            ItemDespawnTimerClientConfig.whitelistedItems.addFirst(id);
            this.list.addToTop(id);
            this.itemIDField.setText("");
        }).dimensions(this.width / 2 + 75, 35, 20, 20).tooltip(Tooltip.of(Text.translatable("item-despawn-timer.midnightconfig.add"))).build();

        this.list = new ItemList(this.client, this.width, this.height - 105, 65, 25);

        for(Identifier identifier : ItemDespawnTimerClientConfig.whitelistedItems){
            this.list.add(identifier);
        }

        this.addDrawableChild(this.itemIDField);
        this.addDrawableChild(this.addEntryButton);
        this.addDrawableChild(this.list);
    }

    @Override
    public void close(){
        this.client.setScreen(this.parentScreen);
    }

    public static class ItemList extends ElementListWidget<ListEntry> {
        public ItemList(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
            super(minecraftClient, width, height, y, itemHeight);
        }

        public void add(Identifier identifier){
            this.addEntry(new ListEntry(identifier, this));
        }

        public void addToTop(Identifier identifier){
            this.addEntryToTop(new ListEntry(identifier, this));
        }

        @Override
        protected void addEntryToTop(ListEntry entry){
            if(this.getScrollAmount() == 0){
                super.addEntryToTop(entry);
                this.setScrollAmount(0);
            } else {
                super.addEntryToTop(entry);
            }
        }

        public void remove(ListEntry listEntry){
            this.removeEntryWithoutScrolling(listEntry);
        }
    }

    public static class ListEntry extends ElementListWidget.Entry<ListEntry> {
        private Identifier identifier;
        private ButtonWidget removeButton;
        private TextWidget itemName;
        private ItemList parentList;
        private MinecraftClient client = MinecraftClient.getInstance();
        private ItemStack representingItem;

        public ListEntry(Identifier identifier, ItemList parentList){
            this.identifier = identifier;
            this.parentList = parentList;

            if(!Registries.ITEM.containsId(this.identifier)){
                this.itemName = new TextWidget(this.parentList.getWidth() / 2 - 70, 0, 135, 25, Text.literal(this.identifier.toString()), this.client.textRenderer);
                this.itemName.alignLeft();
                this.itemName.setTextColor(0x444444);
                this.itemName.setTooltip(Tooltip.of(Text.translatable("item-despawn-timer.midnightconfig.itemMissing")));
            } else {
                this.representingItem = new ItemStack(Registries.ITEM.get(identifier));

                this.itemName = new TextWidget(this.parentList.getWidth() / 2 - 70, 0, 135, 25, Text.translatable(this.representingItem.getTranslationKey()), this.client.textRenderer);
                this.itemName.alignLeft();
            }

            this.removeButton = new ButtonWidget.Builder(Text.literal("-"), button -> {
                ItemDespawnTimerClientConfig.whitelistedItems.remove(this.identifier);
                this.parentList.remove(this);
            }).dimensions(this.parentList.getWidth() / 2 + 75, 0, 20, 20).tooltip(Tooltip.of(Text.translatable("item-despawn-timer.midnightconfig.remove"))).build();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children();
        }

        @Override
        public List<? extends ClickableWidget> children() {
            return List.of(this.itemName, this.removeButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            for(ClickableWidget clickableWidget : this.children()){
                clickableWidget.setY(y);
                clickableWidget.render(context, mouseX, mouseY, tickDelta);
            }

            if(this.representingItem != null){
                context.drawItem(this.representingItem, this.parentList.getWidth() / 2 - 95, y + 3);
            }
        }
    }
}

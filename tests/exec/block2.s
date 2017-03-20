	.text
	.globl main
main:
	subq $16, %rsp
	movq $65, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq $0, %rdi
	cmpq $0, %rdi
	je L18
	movq $66, %rdi
	call putchar
L6:
	movq 0(%rsp), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
L18:
	movq $67, %rdi
	movq $68, %rcx
	movq %rcx, 8(%rsp)
	call putchar
	movq 8(%rsp), %rdi
	call putchar
	jmp L6
	.data
